import { Component, OnInit } from '@angular/core';
import { RegistroService } from '../registro.service';
import { Jugador } from '../jugador';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})

export class RegistroComponent implements OnInit {

  public jugador: Jugador = new Jugador();
  public errores: Jugador = new Jugador();
  public registroCompletado: boolean = false;
  public mensajeRegistroCompletado: string = '';

  constructor(
    private registroService: RegistroService) {
    this.errores.apellidos = '';
    this.errores.telefono = '';
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.registroService.postRegistro(this.jugador)
      .subscribe(
        respuesta => {
          if (respuesta.exito) {
            this.registroCompletado = true;
            this.mensajeRegistroCompletado = respuesta.mensaje;
          } else {
            this.errores = respuesta.contenido;
          }
        }
      );
  }

}
