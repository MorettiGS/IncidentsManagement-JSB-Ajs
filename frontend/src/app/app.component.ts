import { Component } from '@angular/core';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Gestão de Ocorrências';

  constructor(public authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
