import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { RespuestaAPI } from '../modelos/respuesta-api';
import { environment } from 'src/environments/environment';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

export interface PasswordParams {
  titulo?: string;
  dialogo?: string;
  boton?: string;
  token?: string;
  accion?: string;
}

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.css']
})

export class PasswordComponent {

  @Input() params: PasswordParams = {};
  @Output() resultado = new EventEmitter<boolean>();
  
  public pass1 = '';
  public pass2 = '';
  public errorValidacion = '';

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
  ) { }

  onSubmit() {
    this.http
      .post<RespuestaAPI>(`${environment.HOST_ADDR}api/cambiaPassword.php?motivo=${this.params.accion}&token=${this.params.token}`, this.pass1)
      .subscribe((respuesta) => {
        if (respuesta.exito) {
          this.resultado.emit(true);
        } else {
          if (respuesta.contenido.length == 0)
            this.resultado.emit(false);
          else 
            this.errorValidacion = respuesta.contenido;
        }
      });
  }
}
