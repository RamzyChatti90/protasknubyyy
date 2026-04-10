import dayjs from 'dayjs/esm';
import { IProject } from 'app/entities/project/project.model';

export interface ITask {
  id: number;
  title?: string | null;
  description?: string | null;
  status?: string | null;
  priority?: number | null;
  dueDate?: dayjs.Dayjs | null;
  completedAt?: dayjs.Dayjs | null;
  project?: Pick<IProject, 'id'> | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
