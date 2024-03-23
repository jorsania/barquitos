import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'dialogo',
  templateUrl: 'dialogo.component.html',
})
export class DialogoComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public mensaje: string) {}
}