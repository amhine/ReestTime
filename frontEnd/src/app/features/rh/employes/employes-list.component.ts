import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-employes-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './employes-list.component.html'
})
export class EmployesListComponent implements OnInit {
  employes: User[] = [];
  isLoading = true;
  showDeleteModal = false;
  employeToDelete: User | null = null;
  
  searchQuery = '';
  filterDomain = 'ALL';
  sortOrder: 'asc' | 'desc' = 'asc';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadEmployes();
  }

  loadEmployes(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.employes = users.filter(u => u.role === 'EMPLOYE');
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  getFilteredEmployes(): User[] {
    let filtered = this.employes.filter(emp => 
      (`${emp.nom} ${emp.prenom}`.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
      emp.email.toLowerCase().includes(this.searchQuery.toLowerCase())) &&
      (this.filterDomain === 'ALL' || emp.departement === this.filterDomain)
    );

    return filtered.sort((a, b) => {
      const nameA = `${a.nom} ${a.prenom}`.toLowerCase();
      const nameB = `${b.nom} ${b.prenom}`.toLowerCase();
      if (this.sortOrder === 'asc') {
        return nameA.localeCompare(nameB);
      } else {
        return nameB.localeCompare(nameA);
      }
    });
  }

  confirmDelete(employe: User): void {
    this.employeToDelete = employe;
    this.showDeleteModal = true;
  }

  deleteEmploye(): void {
    if (this.employeToDelete) {
      this.userService.deleteUser(this.employeToDelete.id).subscribe({
        next: () => {
          this.loadEmployes();
          this.showDeleteModal = false;
          this.employeToDelete = null;
        }
      });
    }
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
    this.employeToDelete = null;
  }
}
