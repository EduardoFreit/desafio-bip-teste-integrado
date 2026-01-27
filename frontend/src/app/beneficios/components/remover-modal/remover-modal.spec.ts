import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoverModal } from './remover-modal';

describe('RemoverModal', () => {
  let component: RemoverModal;
  let fixture: ComponentFixture<RemoverModal>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemoverModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemoverModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
