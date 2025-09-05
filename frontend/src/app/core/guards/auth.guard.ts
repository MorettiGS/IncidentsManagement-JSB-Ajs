import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable, of, timeout, catchError, map } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean | UrlTree | Observable<boolean | UrlTree> {
    const token = this.authService.getToken();
    if (!token) {
      return this.router.createUrlTree(['/auth/login']);
    }

    const validateFn = (this.authService as any).validateToken;
    if (typeof validateFn === 'function') {
      return validateFn().pipe(
        timeout(3000),
        map(() => true),
        catchError(() => {
          this.authService.logout();
          return of(this.router.createUrlTree(['/auth/login']));
        })
      );
    }

    return true;
  }
}
