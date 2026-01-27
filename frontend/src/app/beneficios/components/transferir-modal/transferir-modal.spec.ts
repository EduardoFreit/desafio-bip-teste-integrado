import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransferirModal } from './transferir-modal';

describe('TransferirModal', () => {
  let component: TransferirModal;
  let fixture: ComponentFixture<TransferirModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransferirModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransferirModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
