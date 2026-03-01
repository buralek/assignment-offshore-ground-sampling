import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Sample } from '../../shared/models/sample.model';

@Component({
  selector: 'app-delete-confirm-dialog',
  imports: [MatButtonModule, MatDialogModule],
  templateUrl: './delete-confirm-dialog.html',
  styleUrl: './delete-confirm-dialog.css',
})
export class DeleteConfirmDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<DeleteConfirmDialogComponent>);
  readonly sample = inject<Sample>(MAT_DIALOG_DATA);

  cancel(): void { this.dialogRef.close(false); }
  confirm(): void { this.dialogRef.close(true); }
}