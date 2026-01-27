import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CriarModal } from './criar-modal';

describe('CriarModal', () => {
  let component: CriarModal;
  let fixture: ComponentFixture<CriarModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CriarModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CriarModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
