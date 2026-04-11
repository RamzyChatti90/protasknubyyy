export interface DashboardStats {
  totalTasks: number;
  completedTasks: number;
  inProgressTasks: number;
  pendingTasks: number;
  taskStatusDistribution: TaskStatusDistribution[];
}

export interface TaskStatusDistribution {
  status: string; // e.g., 'PENDING', 'IN_PROGRESS', 'COMPLETED'
  count: number;
}
