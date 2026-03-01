import { Component, inject } from '@angular/core';
import { AbstractControl, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Location } from '../../shared/models/location.model';
import { Sample, SampleRequest } from '../../shared/models/sample.model';

export interface SampleFormDialogData {
  sample: Sample | null;
  locations: Location[];
}

function notInFuture(control: AbstractControl) {
  const v: Date | null = control.value;
  return v && v > new Date() ? { futureDate: true } : null;
}

function toTimeString(iso: string): string {
  const d = new Date(iso);
  const hh = String(d.getHours()).padStart(2, '0');
  const mm = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  const ms = String(d.getMilliseconds()).padStart(3, '0');
  return `${hh}:${mm}:${ss}.${ms}`;
}

@Component({
  selector: 'app-sample-form-dialog',
  imports: [
    ReactiveFormsModule,
    MatButtonModule, MatDatepickerModule, MatDialogModule,
    MatFormFieldModule, MatInputModule, MatNativeDateModule, MatSelectModule,
  ],
  templateUrl: './sample-form-dialog.html',
  styleUrl: './sample-form-dialog.css',
})
export class SampleFormDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<SampleFormDialogComponent>);
  readonly data = inject<SampleFormDialogData>(MAT_DIALOG_DATA);
  readonly isEditMode = this.data.sample !== null;

  readonly form = inject(FormBuilder).group({
    locationId:    [this.data.sample?.locationId    ?? '',   Validators.required],
    date:          [this.data.sample ? new Date(this.data.sample.timestampWithTimeZone) : null as Date | null,
                   [Validators.required, notInFuture]],
    time:          [this.data.sample ? toTimeString(this.data.sample.timestampWithTimeZone) : '',
                   Validators.required],
    unitWeight:    [this.data.sample?.unitWeight    ?? null as number | null,
                   [Validators.required, Validators.min(0)]],
    waterContent:  [this.data.sample?.waterContent  ?? null as number | null,
                   [Validators.required, Validators.min(0)]],
    shearStrength: [this.data.sample?.shearStrength ?? null as number | null,
                   [Validators.required, Validators.min(0)]],
  });

  cancel(): void {
    this.dialogRef.close();
  }

  save(): void {
    if (this.form.invalid) return;
    const { locationId, date, time, unitWeight, waterContent, shearStrength } = this.form.getRawValue();
    const combined = new Date(date as Date);
    const [timePart, msPart] = (time as string).split('.');
    const [h, m, s = 0] = timePart.split(':').map(Number);
    combined.setHours(h, m, s, msPart ? parseInt(msPart.padEnd(3, '0'), 10) : 0);
    const result: SampleRequest = {
      locationId:         locationId!,
      samplingTimestamp:  combined.toISOString(),
      unitWeight:         +unitWeight!,
      waterContent:       +waterContent!,
      shearStrength:      +shearStrength!,
    };
    this.dialogRef.close(result);
  }
}