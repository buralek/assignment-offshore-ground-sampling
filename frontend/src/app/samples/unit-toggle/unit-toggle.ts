import { Component, model } from '@angular/core';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import {UnitSystem} from '../../shared/models/unit-system.model';

@Component({
  selector: 'app-unit-toggle',
  imports: [MatButtonToggleModule],
  templateUrl: './unit-toggle.html',
  styleUrl: './unit-toggle.css',
})
export class UnitToggleComponent {
  readonly value = model<UnitSystem>('metric');
}
