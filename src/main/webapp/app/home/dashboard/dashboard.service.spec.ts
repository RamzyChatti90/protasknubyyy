import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DashboardService } from './dashboard.service';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { DashboardStats } from './dashboard.model';

describe('DashboardService', () => {
  let service: DashboardService;
  let httpMock: HttpTestingController;
  let applicationConfigService: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        DashboardService,
        {
          provide: ApplicationConfigService,
          useValue: {
            getEndpointFor: (path: string) => `/${path}`, // Mock for testing
          },
        },
      ],
    });

    service = TestBed.inject(DashboardService);
    httpMock = TestBed.inject(HttpTestingController);
    applicationConfigService = TestBed.inject(ApplicationConfigService);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure that there are no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch dashboard stats', () => {
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

    service.getDashboardStats().subscribe(stats => {
      expect(stats).toEqual(dummyStats);
    });

    const req = httpMock.expectOne('/api/dashboard/stats');
    expect(req.request.method).toBe('GET');
    req.flush(dummyStats);
  });
});
