import { User } from './user.model';

export interface Announcement {
  id: number;
  titre: string;
  contenu: string;
  dateCreation: string;
  author?: User;
}

export interface AnnouncementCreate {
  titre: string;
  contenu: string;
}
