import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { MaterialModule } from './shared/material.module';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { IncidentListComponent } from './features/incidents/incident-list/incident-list.component';
import { IncidentDetailComponent } from './features/incidents/incident-detail/incident-detail.component';
import { IncidentFormComponent } from './features/incidents/incident-form/incident-form.component';
import { CommentListComponent } from './features/incidents/comment-list/comment-list.component';
import { CommentFormComponent } from './features/incidents/comment-form/comment-form.component';

import { AuthInterceptor } from './core/interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    IncidentListComponent,
    IncidentDetailComponent,
    IncidentFormComponent,
    CommentListComponent,
    CommentFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule,
    
    MaterialModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
