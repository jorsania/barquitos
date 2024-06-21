import { AppComponent }          from './app.component';
import { AccesoComponent }       from './acceso/acceso.component';
import { InicioComponent }       from './inicio/inicio.component';
import { PartidaComponent }      from './partida/partida.component';
import { AppRoutingModule }      from './app-routing.module';
import { RegistroComponent }     from './registro/registro.component';
import { ActivaCuentaComponent } from './activa-cuenta/activa-cuenta.component';

import { NgModule }              from '@angular/core';
import { BrowserModule }         from '@angular/platform-browser';

import { HttpClientModule }      from '@angular/common/http';

import { FlexLayoutModule }      from '@angular/flex-layout';
import { FormsModule }           from '@angular/forms';

import { MatToolbarModule }      from '@angular/material/toolbar';
import { MatInputModule }        from '@angular/material/input';
import { MatCardModule }         from '@angular/material/card';
import { MatMenuModule }         from '@angular/material/menu';
import { MatIconModule }         from '@angular/material/icon';
import { MatButtonModule }       from '@angular/material/button';
import { MatTableModule }        from '@angular/material/table';
import { MatSlideToggleModule }  from '@angular/material/slide-toggle';
import { MatSelectModule }       from '@angular/material/select';
import { MatOptionModule }       from '@angular/material/core';
import { MatCheckboxModule }     from '@angular/material/checkbox';
import { MatDatepickerModule }   from '@angular/material/datepicker';
import { MatNativeDateModule }   from '@angular/material/core';
import { MatDialogModule }       from '@angular/material/dialog';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RecuperaComponent } from './recupera/recupera.component';
import { RestablecePasswordComponent } from './restablece-password/restablece-password.component';
import { PartidasComponent } from './partidas/partidas.component';

@NgModule({
  declarations: [
    AppComponent,
    AccesoComponent,
    InicioComponent,
    PartidaComponent,
    PartidasComponent,
    RegistroComponent,
    RecuperaComponent,
    ActivaCuentaComponent,
    RestablecePasswordComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    FormsModule,
    MatToolbarModule,
    MatInputModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatOptionModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
