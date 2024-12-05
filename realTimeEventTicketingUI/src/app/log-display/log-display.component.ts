import { Component } from '@angular/core';
import { ControlPanelComponent } from "../control-panel/control-panel.component";

@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [ControlPanelComponent],
  templateUrl: './log-display.component.html',
  styleUrl: './log-display.component.css'
})
export class LogDisplayComponent {
}
