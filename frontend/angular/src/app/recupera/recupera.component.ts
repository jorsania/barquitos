import { Component, OnInit } from '@angular/core';
import { RegistroService } from '../registro.service';
import { Respuesta } from '../respuesta';

@Component({
  selector: 'app-recupera',
  templateUrl: './recupera.component.html',
  styleUrls: ['./recupera.component.css']
})
export class RecuperaComponent implements OnInit {

  public solicitudEnviada: boolean = false;
  public cuenta: string = '';

  constructor(private registroService: RegistroService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.registroService.recuperaCuenta(this.cuenta).subscribe((respuesta: Respuesta) => {
      if (respuesta.exito) this.solicitudEnviada = true;
    });
  }
}
