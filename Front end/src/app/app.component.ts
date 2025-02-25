import {Component, OnInit} from '@angular/core';
import {WowService} from "./wow.service";
import {faHeartPulse} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  isLoggedIn = false;

  constructor(private wowService: WowService ,private router: Router) { }

  ngOnInit(): void {
    this.wowService.init({
      boxClass: 'wow',
      animateClass: 'animated',
      offset: 50,
      mobile: true
    });
    this.checkLoginStatus();
  }
  title = 'iot_front';
    protected readonly heart = faHeartPulse;

  checkLoginStatus() {
    // Check if a token or any authentication identifier exists in local storage
    this.isLoggedIn = !!localStorage.getItem('idSenser');
  }

  reloadPage() {
    window.location.reload();
  }
}
