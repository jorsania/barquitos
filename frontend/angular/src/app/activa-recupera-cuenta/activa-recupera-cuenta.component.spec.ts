import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivaRecuperaCuentaComponent } from './activa-recupera-cuenta.component';

describe('ActivaRecuperaCuentaComponent', () => {
  let component: ActivaRecuperaCuentaComponent;
  let fixture: ComponentFixture<ActivaRecuperaCuentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActivaRecuperaCuentaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivaRecuperaCuentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
