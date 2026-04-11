import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';
import { DashboardService } from './dashboard.service';
import { DashboardStats, TaskStatusDistribution } from './dashboard.model';
import { Subscription } from 'rxjs';

Chart.register(...registerables); // Register all Chart.js components

@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
  dashboardStats: DashboardStats | null = null;
  chart: Chart | null = null;
  @ViewChild('pieChartCanvas') pieChartCanvas!: ElementRef;
  private subscription: Subscription = new Subscription();

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboardStats();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    if (this.chart) {
      this.chart.destroy();
    }
  }

  loadDashboardStats(): void {
    this.subscription.add(
      this.dashboardService.getDashboardStats().subscribe(stats => {
        this.dashboardStats = stats;
        this.updateChart();
      }),
    );
  }

  updateChart(): void {
    if (!this.dashboardStats || !this.pieChartCanvas) {
      return;
    }

    if (this.chart) {
      this.chart.destroy(); // Destroy previous chart instance
    }

    const labels = this.dashboardStats.taskStatusDistribution.map(d => this.getStatusLabel(d.status));
    const data = this.dashboardStats.taskStatusDistribution.map(d => d.count);
    const backgroundColors = this.dashboardStats.taskStatusDistribution.map(d => this.getStatusColor(d.status));

    const ctx = this.pieChartCanvas.nativeElement.getContext('2d');
    this.chart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: labels,
        datasets: [
          {
            data: data,
            backgroundColor: backgroundColors,
            hoverBackgroundColor: backgroundColors,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Distribution des Tâches par Statut',
          },
        },
      },
    });
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'En Attente';
      case 'IN_PROGRESS':
        return 'En Cours';
      case 'COMPLETED':
        return 'Terminées';
      default:
        return status;
    }
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDING':
        return '#FFCE56'; // Yellow
      case 'IN_PROGRESS':
        return '#36A2EB'; // Blue
      case 'COMPLETED':
        return '#4BC0C0'; // Green
      default:
        return '#9966FF'; // Purple for unknown
    }
  }
}
