import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService, UserProfile } from '../../core/services/profile.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  user?: UserProfile;

  constructor(
    private fb: FormBuilder,
    private profileService: ProfileService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
      name: ['', Validators.minLength(2)]
    });
    this.load();
  }

  load() {
    this.loading = true;
    this.profileService.getMe().subscribe({
      next: (u) => {
        this.user = u;
        this.form.patchValue({ email: u.email, name: u.name || '' });
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        console.error('Failed to load profile', err);
      }
    });
  }

  save() {
    if (this.form.invalid) {
      return;
    }
    const updates: Partial<UserProfile> = { name: this.form.value.name };
    this.profileService.updateMe(updates).subscribe({
      next: (res) => {
        this.user = res;
      },
      error: (err) => {
        console.error('Failed to save profile', err);
      }
    });
  }
}
