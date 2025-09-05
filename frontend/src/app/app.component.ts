import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Gestão de Ocorrências';
  currentUser: any = null;
  private sub!: Subscription;

  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.sub = this.auth.getCurrentUser().subscribe(u => this.currentUser = u);
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  goHome(): void {
    this.router.navigate(['/incidents']);
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/auth/login']);
  }
}
