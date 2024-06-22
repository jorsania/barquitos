import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RespuestaAPI } from '../modelos/respuesta-api';
import { environment } from 'src/environments/environment';
import { PasswordParams } from '../password/password.component';

@Component({
  selector: 'app-activa-recupera-cuenta',
  templateUrl: './activa-recupera-cuenta.component.html',
  styleUrls: ['./activa-recupera-cuenta.component.css']
})
export class ActivaRecuperaCuentaComponent implements OnInit {

  public tokenValido: boolean = false;
  public tokenInvalido: boolean = false;
  public finalizado: boolean = false;
  public passwordParams: PasswordParams = {};
  public despedida: string = '';

  private token: string;
  private accion: string;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute
  ) {
    this.accion = this.route.snapshot.url[0].path; 
    this.token = this.route.snapshot.params['token'];
  }

  ngOnInit(): void {
    this.http
      .get<RespuestaAPI>(`${environment.HOST_ADDR}api/cambiaPassword.php?motivo=${this.accion}&token=${this.token}`)
      .subscribe(respuesta => this.solicitaAutorizacion(respuesta));
  }

  private solicitaAutorizacion(respuesta: RespuestaAPI): void {
    if (respuesta.exito) {
      if (this.accion == 'activa') {
        this.passwordParams = {
          titulo: 'Completa el registro',
          dialogo: 
            `Establece una contraseña para completar el registro de la cuenta '${respuesta.contenido.alias}' 
             asociada al email '${respuesta.contenido.email}'`,
          boton: 'Activa la cuenta'
        };
        this.despedida = 
          `La cuenta ha sido activada correctamente, a continuación serás redirigido a la página de
           acceso donde podrás acceder al juego con tus nuevas credenciales`;
      } else if (this.accion == 'recupera') {
        this.passwordParams = {
          titulo: 'Restablece la contraseña',
          dialogo:
            `Elige una nueva contraseña para la cuenta '${respuesta.contenido.alias}'
             asociada al email '${respuesta.contenido.email}'`,
          boton: 'Restablece contraseña'
        };
        this.despedida = 
          `La contraseña ha sido restablecida correctamente, a continuación serás redirigido a la página de
           acceso donde podrás acceder al juego con tus nuevas credenciales`;
      }
      this.passwordParams.token = this.token;
      this.passwordParams.accion = this.accion;
      this.tokenValido = true;
    } else {
      this.tokenInvalido = true;
    }
  }

  resultado(passEstablecido: boolean) {
    if (passEstablecido) {
      this.tokenValido = false;
      this.finalizado = true;
    } else {
      this.tokenInvalido = true;
    }
  }
}
