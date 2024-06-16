import { Component, OnInit } from '@angular/core';
import { RegistroService } from '../registro.service';
import { ActivatedRoute } from '@angular/router';
import { Respuesta } from '../respuesta';

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
    private registroService: RegistroService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.token = params['token'];
      if (this.token) 
        this.registroService.compruebaTokenRecuperacion(this.token).
          subscribe((respuesta: Respuesta) => {
            if (respuesta.exito) {
              this.alias = respuesta.contenido.alias;
              this.email = respuesta.contenido.email;
              this.tokenValido = true;
            }
          });
   });    
  }

  onSubmit() {
    this.registroService.restablecePassword(this.token as string, this.pass1).
      subscribe((respuesta: Respuesta) => {
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
