import { HttpClient } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, catchError, from, map, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { Respuesta } from './respuesta';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AutentificaService implements OnDestroy {

  private _notificador$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public get isAuthenticated$(): Observable<boolean> {
    return this._notificador$.asObservable();
  }

  constructor(private _router: Router, private http: HttpClient) {
    this.http.get(`${environment.DIRECCION}/api/autentifica.php`).subscribe(
      (respuesta: any) => this._notificador$.next(respuesta.autentificado));
  }

  public ngOnDestroy(): void {
    this._notificador$.next(false);
    this._notificador$.complete();
  }

  public login(login: string, pass: string, permanente: number): Observable<Respuesta> {
    return from(this.http.post<Respuesta>(`${environment.DIRECCION}/api/autentifica.php`,
      {
        login: login,
        pass: pass,
        permanente: permanente
      }))
      .pipe(map((respuesta : Respuesta) => this.manejaRespuesta(respuesta)));
  }

  public logout(redirect: string): Observable<void> {
    return from(this.http.get(`${environment.DIRECCION}/api/desconecta.php`))
      .pipe(map(_ => {
        this._notificador$.next(false);
        this._router.navigate([redirect])
      }));
  }

  private manejaRespuesta(respuesta: Respuesta): Respuesta {
    if (!respuesta.exito) {
      throw new Error("Error en el proceso de autenticación:\n" + respuesta.mensaje);
    }
    this._notificador$.next(respuesta.autentificado);
    return respuesta
  }
}
