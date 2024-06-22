import { Component, OnInit } from '@angular/core';
import { RespuestaAPI } from '../modelos/respuesta-api';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-recupera',
  templateUrl: './recupera.component.html',
  styleUrls: ['./recupera.component.css']
})
export class RecuperaComponent implements OnInit {

  public solicitudEnviada: boolean = false;
  public cuenta: string = '';

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.http
      .post<RespuestaAPI>(`${environment.HOST_ADDR}api/recuperaCuenta.php`, this.cuenta)
      .subscribe((respuesta: RespuestaAPI) => {
        if (respuesta.exito) this.solicitudEnviada = true
      });
  }
}
