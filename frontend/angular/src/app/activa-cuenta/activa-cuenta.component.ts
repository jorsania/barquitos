import { Component, OnInit } from '@angular/core';
import { RegistroService } from '../registro.service';
import { ActivatedRoute } from '@angular/router';
import { Respuesta } from '../respuesta';

@Component({
  selector: 'app-activa-cuenta',
  templateUrl: './activa-cuenta.component.html',
  styleUrls: ['./activa-cuenta.component.css']
})
export class ActivaCuentaComponent implements OnInit {

  private sub: any;
  private token: string | null = null;
  public tokenValido: boolean | null = null;
  public cuentaActivada: boolean = false;
  public pass1 = '';
  public pass2 = '';
  public alias = '';
  public email = '';
  public errorValidacion = '';

  constructor(
    private route: ActivatedRoute,
    private registroService: RegistroService) { }

  ngOnInit(): void {
    this.sub = this.route.params.subscribe(params => {
      this.token = params['token'];
      if (this.token) 
        this.registroService.compruebaToken(this.token).
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
    this.registroService.activaCuenta(this.token as string, this.pass1).
      subscribe((respuesta: Respuesta) => {
        if (respuesta.exito) {
          this.alias = respuesta.contenido.alias;
          this.email = respuesta.contenido.email;
          this.tokenValido = null;
          this.cuentaActivada = true;
        } else {
          this.errorValidacion = respuesta.contenido;
        }
      });
  }
}
