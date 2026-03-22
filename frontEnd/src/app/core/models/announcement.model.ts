export interface Announcement {
  id: number;
  titre: string;
  contenu: string;
  dateCreation: string;
}

export interface AnnouncementCreate {
  titre: string;
  contenu: string;
}
