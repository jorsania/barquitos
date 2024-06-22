import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { AutentificaService } from '../servicios/autentifica.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-partidas',
  templateUrl: './partidas.component.html',
  styleUrls: ['./partidas.component.css']
})
export class PartidasComponent implements OnInit {

  public partidas: any[] = [];
  public displayedColumns: string[] = ['idPartida', 'nJugadores', 'jugador1', 'jugador2', 'turno', 'fechaCrea', 'fechaMod'];
  public isAuthenticated = false;

  private _destroySub$ = new Subject<void>();

  constructor(
    private http: HttpClient,
    private autentificaService: AutentificaService) {
    this.autentificaService.isAuthenticated$
      .pipe(takeUntil(this._destroySub$))
      .subscribe((isAuthenticated: boolean) => {
        this.isAuthenticated = isAuthenticated
        this.http.get<any[]>(`${environment.HOST_ADDR}api/misPartidas.php`)
          .subscribe((data) => this.partidas = data);
      });
  }

  ngOnInit(): void {
    if (this.isAuthenticated)
      this.http.get<any[]>(`${environment.HOST_ADDR}api/misPartidas.php`)
        .subscribe((data) => this.partidas = data);
  }

  ngOnDestroy() {
    this._destroySub$.next();
  }
}