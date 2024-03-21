import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivaCuentaComponent } from './activa-cuenta.component';

describe('ActivaCuentaComponent', () => {
  let component: ActivaCuentaComponent;
  let fixture: ComponentFixture<ActivaCuentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActivaCuentaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivaCuentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
