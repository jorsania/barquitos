import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RespuestaAPI } from '../modelos/respuesta-api';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-restablece-password',
  templateUrl: './restablece-password.component.html',
  styleUrls: ['./restablece-password.component.css']
})
export class RestablecePasswordComponent implements OnInit {

  private token: string | null = null;
  public tokenValido: boolean | null = null;
  public passRestablecida: boolean = false;
  public pass1 = '';
  public pass2 = '';
  public alias = '';
  public email = '';
  public errorValidacion = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.token = params['token'];
      if (this.token) 
        this.http
          .get<RespuestaAPI>(`${environment.HOST_ADDR}api/restablecePassword.php?token=${this.token}`)
          .subscribe((respuesta: RespuestaAPI) => {
            if (respuesta.exito) {
              this.alias = respuesta.contenido.alias;
              this.email = respuesta.contenido.email;
              this.tokenValido = true;
            }
          });
   });    
  }

  onSubmit() {
    this.http
      .post<RespuestaAPI>(`${environment.HOST_ADDR}api/restablecePassword.php?token=${this.token}`, this.pass1)
      .subscribe((respuesta: RespuestaAPI) => {
        if (respuesta.exito) {
          this.alias = respuesta.contenido.alias;
          this.email = respuesta.contenido.email;
          this.tokenValido = null;
          this.passRestablecida = true;
        } else {
          this.errorValidacion = respuesta.contenido;
        }
      });
  }
}
