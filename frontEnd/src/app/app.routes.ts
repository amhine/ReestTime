import { Routes } from '@angular/router';
import { authGuard, rhGuard, employeGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./features/auth/forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent)
  },
  {
    path: 'reset-password',
    loadComponent: () => import('./features/auth/reset-password/reset-password.component').then(m => m.ResetPasswordComponent)
  },
  {
    path: 'rh',
    canActivate: [rhGuard],
    loadComponent: () => import('./features/rh/layout/rh-layout.component').then(m => m.RhLayoutComponent),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/rh/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'employes',
        loadComponent: () => import('./features/rh/employes/employes-list.component').then(m => m.EmployesListComponent)
      },
      {
        path: 'employes/nouveau',
        loadComponent: () => import('./features/rh/employes/employe-form.component').then(m => m.EmployeFormComponent)
      },
      {
        path: 'employes/:id/modifier',
        loadComponent: () => import('./features/rh/employes/employe-form.component').then(m => m.EmployeFormComponent)
      },
      {
        path: 'announcements',
        loadComponent: () => import('./features/rh/announcements/announcement-list.component').then(m => m.AnnouncementListComponent)
      },
      {
        path: 'demandes',
        loadComponent: () => import('./features/rh/demandes/demandes-validation-list.component').then(m => m.DemandesValidationListComponent)
      },
      {
        path: 'historique',
        loadComponent: () => import('./features/rh/historique/historique-list.component').then(m => m.HistoriqueListComponent)
      }
    ]
  },
  {
    path: 'employe',
    canActivate: [employeGuard],
    loadComponent: () => import('./features/employe/layout/employe-layout.component').then(m => m.EmployeLayoutComponent),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/employe/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'demandes',
        loadComponent: () => import('./features/employe/demandes/demandes-list.component').then(m => m.DemandesListComponent)
      },
      {
        path: 'demandes/nouvelle',
        loadComponent: () => import('./features/employe/demandes/demande-form.component').then(m => m.DemandeFormComponent)
      },
      {
        path: 'demandes/:id/modifier',
        loadComponent: () => import('./features/employe/demandes/demande-form.component').then(m => m.DemandeFormComponent)
      },
      {
        path: 'attendance',
        loadComponent: () => import('./features/employe/attendance/attendance.component').then(m => m.AttendanceComponent)
      },
      {
        path: 'calendar',
        loadComponent: () => import('./features/employe/calendar/calendar.component').then(m => m.CalendarComponent)
      },
      {
        path: 'profil',
        loadComponent: () => import('./features/employe/profil/profil.component').then(m => m.ProfilComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
