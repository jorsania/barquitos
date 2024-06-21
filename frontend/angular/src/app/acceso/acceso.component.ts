import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AutentificaService } from '../servicios/autentifica.service';
import { filter, Subject, take, takeUntil } from 'rxjs';

@Component({
  selector: 'app-acceso',
  templateUrl: './acceso.component.html',
  styleUrls: ['./acceso.component.css']
})
export class AccesoComponent implements OnInit, OnDestroy {

  public login = '';
  public password = '';
  public recuerdame = false;
  public mensaje = '';

  private readonly returnUrl: string;
  private _destroySub$ = new Subject<void>();

  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    private _autentificaService: AutentificaService
  ) {
    this.returnUrl = this._route.snapshot.queryParams['returnUrl'] || '/';
  }

  public ngOnInit(): void {
    this._autentificaService.isAuthenticated$.pipe(
      filter((isAuthenticated: boolean) => isAuthenticated),
      takeUntil(this._destroySub$)
    ).subscribe(_ => this._router.navigateByUrl(this.returnUrl));
  }

  public ngOnDestroy(): void {
    this._destroySub$.next();
  }

  public onSubmit(): void {
    this._autentificaService.login(
      this.login,
      this.password,
      this.recuerdame ? 1 : 0)
      .pipe(take(1)).subscribe(respuesta => {
        respuesta.autentificado;
        if (respuesta.autentificado) 
          this._router.navigateByUrl(this.returnUrl);
          else {
            this.mensaje = respuesta.mensaje;
          }
      });
  }
}