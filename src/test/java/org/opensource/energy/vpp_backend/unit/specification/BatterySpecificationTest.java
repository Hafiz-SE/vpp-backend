package org.opensource.energy.vpp_backend.unit.specification;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensource.energy.vpp_backend.entity.Battery;
import org.opensource.energy.vpp_backend.specification.BatterySpecification;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BatterySpecificationTest {

    @Mock
    private Root<Battery> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Path<Integer> postcodePath;

    @Mock
    private Path<Long> wattCapacityPath;

    @Captor
    private ArgumentCaptor<Predicate[]> predicatesCaptor;

    @BeforeEach
    public void setup() {
        lenient().when(root.<Integer>get("postcode")).thenReturn(postcodePath);
        lenient().when(root.<Long>get("wattCapacity")).thenReturn(wattCapacityPath);

        lenient().when(cb.between(any(), anyInt(), anyInt())).thenReturn(mock(Predicate.class));
        lenient().when(cb.between(any(), anyLong(), anyLong())).thenReturn(mock(Predicate.class));
        lenient().when(cb.and(any(Predicate[].class))).thenReturn(mock(Predicate.class));
    }

    @Test
    public void given_postcode_and_wattage_parameters_when_creating_specification_then_both_predicates_are_created() {
        int postcodeFrom = 1000;
        int postcodeTo = 2000;
        Long wattageFrom = 5000L;
        Long wattageTo = 10000L;

        Specification<Battery> spec = BatterySpecification.byPostcodeAndWattageRange(
                postcodeFrom, postcodeTo, wattageFrom, wattageTo);
        spec.toPredicate(root, query, cb);

        verify(cb).between(postcodePath, postcodeFrom, postcodeTo);
        verify(cb).between(wattCapacityPath, wattageFrom, wattageTo);
        verify(cb).and(predicatesCaptor.capture());
        assertEquals(2, predicatesCaptor.getValue().length);
    }

    @Test
    public void given_only_postcode_parameters_when_creating_specification_then_only_postcode_predicate_is_created() {
        int postcodeFrom = 3000;
        int postcodeTo = 4000;
        Long wattageFrom = null;
        Long wattageTo = null;

        Specification<Battery> spec = BatterySpecification.byPostcodeAndWattageRange(
                postcodeFrom, postcodeTo, wattageFrom, wattageTo);
        spec.toPredicate(root, query, cb);

        verify(cb).between(postcodePath, postcodeFrom, postcodeTo);
        verify(cb).and(predicatesCaptor.capture());
        assertEquals(1, predicatesCaptor.getValue().length);
    }

    @Test
    public void given_all_parameters_when_creating_specification_then_correct_parameter_values_are_used() {
        int postcodeFrom = 5000;
        int postcodeTo = 6000;
        Long wattageFrom = 7000L;
        Long wattageTo = 8000L;

        ArgumentCaptor<Integer> postcodeFromCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> postcodeToCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Long> wattageFromCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> wattageToCaptor = ArgumentCaptor.forClass(Long.class);

        Specification<Battery> spec = BatterySpecification.byPostcodeAndWattageRange(
                postcodeFrom, postcodeTo, wattageFrom, wattageTo);
        spec.toPredicate(root, query, cb);

        verify(cb).between(eq(postcodePath), postcodeFromCaptor.capture(), postcodeToCaptor.capture());
        assertEquals(postcodeFrom, postcodeFromCaptor.getValue());
        assertEquals(postcodeTo, postcodeToCaptor.getValue());

        verify(cb).between(eq(wattCapacityPath), wattageFromCaptor.capture(), wattageToCaptor.capture());
        assertEquals(wattageFrom, wattageFromCaptor.getValue());
        assertEquals(wattageTo, wattageToCaptor.getValue());
    }
}