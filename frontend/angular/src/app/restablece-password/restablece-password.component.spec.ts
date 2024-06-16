import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestablecePasswordComponent } from './restablece-password.component';

describe('RestablecePasswordComponent', () => {
  let component: RestablecePasswordComponent;
  let fixture: ComponentFixture<RestablecePasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RestablecePasswordComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RestablecePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
