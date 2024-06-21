import { Component, OnInit } from '@angular/core';
import { Jugador } from '../modelos/jugador';
import { RespuestaAPI } from '../modelos/respuesta-api';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})

export class RegistroComponent implements OnInit {

  public jugador: Jugador = new Jugador();
  public errores: Jugador = new Jugador();
  public registroCompletado: boolean = false;

  constructor(private http: HttpClient) {
    this.errores.apellidos = '';
    this.errores.telefono = '';
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.http
      .post<RespuestaAPI>(`${environment.HOST_ADDR}api/registraJugador.php`,this.jugador)
      .subscribe(respuesta => {
        if (respuesta.exito) {
          this.registroCompletado = true;
        } else {
          this.errores = respuesta.contenido;
        } 
      }); 
  }
}
