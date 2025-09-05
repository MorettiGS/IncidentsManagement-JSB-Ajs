import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProfileService, UserProfile } from '../../core/services/profile.service';
import { ToastrService } from 'ngx-toastr';

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
    private toastr: ToastrService
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
        this.toastr.error('Failed to load profile');
      }
    });
  }

  save() {
    if (this.form.invalid) {
      this.toastr.warning('Please fix the fields');
      return;
    }
    const updates: Partial<UserProfile> = { name: this.form.value.name };
    this.profileService.updateMe(updates).subscribe({
      next: (res) => {
        this.user = res;
        this.toastr.success('Profile saved');
      },
      error: (err) => {
        console.error('Failed to save profile', err);
        this.toastr.error('Failed to save profile');
      }
    });
  }
}
