import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { RespuestaJuego } from '../modelos/respuesta-juego';
import { AutentificaService } from '../servicios/autentifica.service';
import { Subject, takeUntil } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-partida',
  templateUrl: './partida.component.html',
  styleUrls: ['./partida.component.css'],
})

export class PartidaComponent implements OnInit {

  private socket: WebSocket;
  private _destroySub$ = new Subject<void>();

  public turno?: boolean | null;

  public isAuthenticated = false;
  public ganador: boolean = false;
  public perdedor: boolean = false;
  public tableroPropio: String[][];
  public tableroOponente: String[][];
  public notificaTurnoJugador: boolean = false;
  public notificaTurnoOponente: boolean = false;

  constructor(
    private _router: Router,
    private autentificaService: AutentificaService
  ) {
    this.socket = new WebSocket(`${environment.HOST_ADDR}partida/ws`);
    this.tableroPropio = Array.from({ length: 10 }, () => Array.from({ length: 10 }, () => ''));
    this.tableroOponente = Array.from({ length: 10 }, () => Array.from({ length: 10 }, () => ''));
    this.autentificaService.isAuthenticated$.pipe(
      takeUntil(this._destroySub$)
    ).subscribe((isAuthenticated: boolean) => this.isAuthenticated = isAuthenticated);
  };

  public dispara(x: number, y: number) {
    if (this.turno) this.socket.send(JSON.stringify({ "disparo": { "x": x + 1, "y": y + 1 } }))
  }

  ngOnInit(): void {
    this.socket
      .onmessage =(event) => {
        const msg: RespuestaJuego = JSON.parse(event.data);
        if (msg.TableroPropio) this.tableroPropio = msg.TableroPropio;
        if (msg.TableroOponente) this.tableroOponente = msg.TableroOponente;
        if ((msg.turno && msg.turno != this.turno) || typeof this.turno == 'undefined') {
          this.turno = msg.turno;
          if (msg.turno == true) {
            this.notificaTurnoJugador = true;
            setTimeout(() => { this.notificaTurnoJugador = false; }, 1000);
          } else {
            this.notificaTurnoOponente = true;
            setTimeout(() => { this.notificaTurnoOponente = false; }, 1000);
          }
        }
        if (msg.ganador) {
          msg.ganador ? this.ganador = true : this.perdedor = true;
          this.socket.close();
        }
      }
  }

  ngOnDestroy() {
    this.socket.close();
    this._destroySub$.next();
  }
}
