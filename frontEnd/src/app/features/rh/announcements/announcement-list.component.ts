import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AnnouncementService } from '../../../core/services/announcement.service';
import { Announcement } from '../../../core/models/announcement.model';

@Component({
  selector: 'app-announcement-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './announcement-list.component.html'
})
export class AnnouncementListComponent implements OnInit {
  announcements: Announcement[] = [];
  isLoading = true;
  isSubmitting = false;
  showModal = false;
  isEditing = false;
  selectedId: number | null = null;
  announcementForm!: FormGroup;

  constructor(
    private announcementService: AnnouncementService,
    private fb: FormBuilder
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.loadAnnouncements();
  }

  initForm(): void {
    this.announcementForm = this.fb.group({
      titre: ['', [Validators.required, Validators.maxLength(100)]],
      contenu: ['', [Validators.required, Validators.maxLength(1000)]]
    });
  }

  loadAnnouncements(): void {
    this.isLoading = true;
    this.announcementService.getAllAnnouncements().subscribe({
      next: (data) => {
        this.announcements = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement annonces', err);
        this.isLoading = false;
      }
    });
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.selectedId = null;
    this.announcementForm.reset();
    this.showModal = true;
  }

  openEditModal(annonce: Announcement): void {
    this.isEditing = true;
    this.selectedId = annonce.id;
    this.announcementForm.patchValue({
      titre: annonce.titre,
      contenu: annonce.contenu
    });
    this.showModal = true;
  }

  onSubmit(): void {
    if (this.announcementForm.invalid) return;
    this.isSubmitting = true;

    if (this.isEditing && this.selectedId) {
      this.announcementService.updateAnnouncement(this.selectedId, this.announcementForm.value).subscribe({
        next: (updated) => {
          const index = this.announcements.findIndex(a => a.id === this.selectedId);
          this.announcements[index] = updated;
          this.closeModal();
        },
        error: (err) => { console.error(err); this.isSubmitting = false; }
      });
    } else {
      this.announcementService.createAnnouncement(this.announcementForm.value).subscribe({
        next: (newAnnonce) => {
          this.announcements.unshift(newAnnonce);
          this.closeModal();
        },
        error: (err) => { console.error(err); this.isSubmitting = false; }
      });
    }
  }

  closeModal(): void {
    this.showModal = false;
    this.isSubmitting = false;
  }

  deleteAnnouncement(id: number): void {
    if (confirm('Supprimer cette annonce ?')) {
      this.announcementService.deleteAnnouncement(id).subscribe(() => {
        this.announcements = this.announcements.filter(a => a.id !== id);
      });
    }
  }
}
