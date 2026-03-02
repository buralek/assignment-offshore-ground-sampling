import { Component, inject } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { UnitSystemService } from '../../shared/services/unit-system.service';

@Component({
  selector: 'app-unit-toggle',
  imports: [MatButtonToggleModule],
  templateUrl: './unit-toggle.html',
  styleUrl: './unit-toggle.css',
})
export class UnitToggleComponent {
  readonly unitSystemService = inject(UnitSystemService);
}
