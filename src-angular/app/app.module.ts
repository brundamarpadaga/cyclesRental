import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CyclesComponent } from './cycles/cycles.component';
import { HttpClientModule } from '@angular/common/http';
import { BorrowedCyclesComponent } from './borrowed-cycles/borrowed-cycles.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RestockComponent } from './restock/restock.component';
import { ReturnComponent } from './return/return.component';

@NgModule({
  declarations: [
    AppComponent,
    CyclesComponent,
    BorrowedCyclesComponent,
    NavBarComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    RestockComponent,
    ReturnComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
