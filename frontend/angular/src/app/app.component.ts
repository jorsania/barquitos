import { Component } from '@angular/core';
import { Subject, take, takeUntil } from 'rxjs';
import { AutentificaService } from './servicios/autentifica.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'Juego de los Barquitos';
  public isAuthenticated = false;
  private _destroySub$ = new Subject<void>();

  constructor(private _autentificaService: AutentificaService) { }

  public ngOnInit(): void {
    this._autentificaService.isAuthenticated$
      .pipe(takeUntil(this._destroySub$))
      .subscribe((isAuthenticated: boolean) => this.isAuthenticated = isAuthenticated);
  }

  public ngOnDestroy(): void {
    this._destroySub$.next();
  }

  public logout(): void {
    this._autentificaService.logout('/').pipe(take(1))
      .subscribe(_ => this.isAuthenticated = false);
  }
}
