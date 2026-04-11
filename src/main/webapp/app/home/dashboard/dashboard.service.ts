import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { DashboardStats } from './dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dashboard/stats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(this.resourceUrl);
  }
}
