import { TestBed } from '@angular/core/testing';

import { AutentificaService } from './autentifica.service';

describe('AutentificaService', () => {
  let service: AutentificaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AutentificaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
