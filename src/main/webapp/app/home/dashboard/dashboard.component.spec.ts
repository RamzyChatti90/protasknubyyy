import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { DashboardComponent } from './dashboard.component';
import { DashboardService } from './dashboard.service';
import { DashboardStats } from './dashboard.model';
import { ElementRef } from '@angular/core';
import { Chart, registerables } from 'chart.js';

// Mock the Chart.js module
jest.mock('chart.js', () => {
  const MockChart = jest.fn().mockImplementation(() => ({
    destroy: jest.fn(),
  }));
  (MockChart as any).register = jest.fn(); // Mock the static register method
  return {
    Chart: MockChart,
    registerables: [], // Provide an empty array as registerables are mocked for testing purposes
  };
});

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let mockDashboardService: any;

  const dummyStats: DashboardStats = {
    totalTasks: 10,
    completedTasks: 5,
    inProgressTasks: 3,
    pendingTasks: 2,
    taskStatusDistribution: [
      { status: 'COMPLETED', count: 5 },
      { status: 'IN_PROGRESS', count: 3 },
      { status: 'PENDING', count: 2 },
    ],
  };

  beforeEach(async () => {
    mockDashboardService = {
      getDashboardStats: jest.fn().mockReturnValue(of(dummyStats)),
    };

    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [{ provide: DashboardService, useValue: mockDashboardService }],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;

    // Mock the ElementRef for the canvas
    component.pieChartCanvas = new ElementRef(document.createElement('canvas'));

    fixture.detectChanges(); // ngOnInit will be called here
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load dashboard stats on init', () => {
    expect(mockDashboardService.getDashboardStats).toHaveBeenCalled();
    expect(component.dashboardStats).toEqual(dummyStats);
  });

  it('should initialize and update the chart', () => {
    // Ensure Chart.register is called by the component with whatever registerables it imports
    expect((Chart as any).register).toHaveBeenCalledWith(...registerables);

    // Check if Chart constructor was called with correct data
    expect(Chart).toHaveBeenCalledTimes(1);
    const chartArgs = (Chart as jest.Mock).mock.calls[0];
    expect(chartArgs[1].data.labels).toEqual(['Terminées', 'En Cours', 'En Attente']);
    expect(chartArgs[1].data.datasets[0].data).toEqual([5, 3, 2]);
    expect(chartArgs[1].type).toBe('pie');
  });

  it('should destroy chart on ngOnDestroy', () => {
    // Create a mock chart instance that has a destroy method
    const mockChartInstance = { destroy: jest.fn() };
    component.chart = mockChartInstance as any; // Cast to any because Chart is mocked

    component.ngOnDestroy();
    expect(mockChartInstance.destroy).toHaveBeenCalled();
  });

  it('should display loading message if stats are null', () => {
    component.dashboardStats = null;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.loading-message')).toBeTruthy();
    expect(compiled.querySelector('.stats-summary')).toBeFalsy();
    expect(compiled.querySelector('.chart-section')).toBeFalsy();
  });

  it('should display no data message if taskStatusDistribution is empty', () => {
    component.dashboardStats = { ...dummyStats, taskStatusDistribution: [] };
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.no-data-message')).toBeTruthy();
    expect(compiled.querySelector('.chart-section')).toBeFalsy();
  });
});
