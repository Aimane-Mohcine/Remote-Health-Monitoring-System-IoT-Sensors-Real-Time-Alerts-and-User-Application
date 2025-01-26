import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {InscreptionComponent} from "./inscreption/inscreption.component";
import {LoginComponent} from "./login/login.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {ProfileComponent} from "./profile/profile.component";
import {HestoriqueComponent} from "./hestorique/hestorique.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {AnomaliComponent} from "./anomali/anomali.component";
import {ChartComponent} from "./chart/chart.component";
import {TopWidgeComponent} from "./top-widge/top-widge.component";

const routes: Routes = [

  { path: '',title:'Home', component: HomeComponent },
  { path: 'Inscreption',title:'Inscreption', component: InscreptionComponent },
  { path: 'Login',title:'Login', component: LoginComponent },
  { path: 'Dashboard',title:'Dashboard', component: DashboardComponent },
  { path: 'Profile',title:'Profile', component: ProfileComponent },
  { path: 'Profile_home',title:'Profile-home', component: SidebarComponent },
  { path: 'Anomali',title:'Anomali', component: AnomaliComponent },
  { path: 'Chart',title:'Chart', component: ChartComponent },
  { path: 'TopWidge',title:'TopWidge', component: TopWidgeComponent },
  { path: 'Hestorique',title:'Hestorique ', component: HestoriqueComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
