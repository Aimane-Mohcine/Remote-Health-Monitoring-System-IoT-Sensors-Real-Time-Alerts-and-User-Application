import { NgModule } from '@angular/core';
import {BrowserModule, provideClientHydration} from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './login/login.component';
import { InscreptionComponent } from './inscreption/inscreption.component';
import { ProfileComponent } from './profile/profile.component';
import { HestoriqueComponent } from './hestorique/hestorique.component';
import {NgForOf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {provideHttpClient} from "@angular/common/http";
import { SidebarComponent } from './sidebar/sidebar.component';
import { TopWidgeComponent } from './top-widge/top-widge.component';
import { ChartComponent } from './chart/chart.component';
import { AnomaliComponent } from './anomali/anomali.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {ChartModule} from "angular-highcharts";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    DashboardComponent,
    LoginComponent,
    InscreptionComponent,
    ProfileComponent,
    HestoriqueComponent,
    SidebarComponent,
    TopWidgeComponent,
    ChartComponent,
    AnomaliComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgForOf,
    FormsModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    ChartModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
