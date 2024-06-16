import { Component, OnInit } from '@angular/core';
import { PartidaService } from '../partida.service';

@Component({
  selector: 'app-partida',
  templateUrl: './partida.component.html',
  styleUrls: ['./partida.component.css']
})

export class PartidaComponent implements OnInit {

  turno: boolean | null = null;
  ganador: boolean = false;
  perdedor: boolean = false;
  notificaTurnoJugador: boolean = false;
  notificaTurnoOponente: boolean = false;
  tableroPropio: String[][];
  tableroOponente: String[][];

  constructor(private partidaService: PartidaService) {
    this.tableroPropio = Array.from({ length: 10 }, () => Array.from({ length: 10 }, () => ''));
    this.tableroOponente = Array.from({ length: 10 }, () => Array.from({ length: 10 }, () => ''));
    this.partidaService.connect().subscribe({
      next: (msg) => {
        if (typeof msg.TableroPropio !== 'undefined') this.tableroPropio = msg.TableroPropio;
        if (typeof msg.TableroOponente !== 'undefined') this.tableroOponente = msg.TableroOponente;
        if (typeof msg.turno !== 'undefined' && msg.turno != this.turno) {
          this.turno = msg.turno;
          if (msg.turno == true) {
            this.notificaTurnoJugador = true;
            setTimeout(() => { this.notificaTurnoJugador = false; }, 1000);
          } else {
            this.notificaTurnoOponente = true;
            setTimeout(() => { this.notificaTurnoOponente = false; }, 1000);
          }
        }
        if (typeof msg.ganador !== 'undefined') {
          msg.ganador ? this.ganador = true : this.perdedor = true;
          this.partidaService.close();
        }

      }
    });
  }

  ngOnInit(): void {
    if (this.partidaService.isOpen()) this.partidaService.sendMessage(JSON.stringify({ "situacion": null }));
  }

  public dispara(x: number, y: number) {
    if (this.turno) this.partidaService.sendMessage(JSON.stringify({ "disparo": { "x": x + 1, "y": y + 1 } }))
  }
}
