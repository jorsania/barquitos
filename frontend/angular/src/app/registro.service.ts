import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Jugador } from "./jugador"
import { Respuesta } from "./respuesta"
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegistroService {

  constructor(private http: HttpClient) { }

  postRegistro(registro: Jugador) {
    return this.http.post<Respuesta>(`${environment.HOST_ADDR}api/registraJugador.php`, registro);
  }

  compruebaToken(token: string) {
    return this.http.get<Respuesta>(`${environment.HOST_ADDR}api/activaCuenta.php?token=${token}`);
  }

  activaCuenta(token: string, password: string) {
    return this.http.post<Respuesta>(`${environment.HOST_ADDR}api/activaCuenta.php?token=${token}`,password);
  }

}