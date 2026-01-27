import { Routes } from '@angular/router';
import { Beneficios } from './beneficios/beneficios';

export const routes: Routes = [
  { path: 'beneficios', component: Beneficios },
  { path: '', redirectTo: 'beneficios', pathMatch: 'full' }
];
