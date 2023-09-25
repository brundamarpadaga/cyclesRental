import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from '/home/brunda/AngularCycleShop/src/app/home/home.component';
import { BorrowedCyclesComponent } from './borrowed-cycles/borrowed-cycles.component';
import { RestockComponent } from './restock/restock.component';
import { ReturnComponent } from './return/return.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';



const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'restock', component: RestockComponent },
  { path: 'return', component: ReturnComponent },
  { path: 'rent', component: BorrowedCyclesComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
