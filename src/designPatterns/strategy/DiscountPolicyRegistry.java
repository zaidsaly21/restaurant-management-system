package designPatterns.strategy;

import solid.ocp.DiscountPolicy;
import solid.ocp.DatedHappyHourDiscountPolicy;
import solid.ocp.PercentageDiscountPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public final class DiscountPolicyRegistry {

    private static final DiscountPolicyRegistry INSTANCE = new DiscountPolicyRegistry();
    private final AtomicInteger idSeq = new AtomicInteger(1);
    private final List<RegisteredPolicy> policies = new ArrayList<>();

    private DiscountPolicyRegistry() {}

    public static DiscountPolicyRegistry getInstance() {
        return INSTANCE;
    }

    
    public synchronized RegisteredPolicy add(String name, DiscountPolicy policy) {
        RegisteredPolicy rp = new RegisteredPolicy(idSeq.getAndIncrement(), name, policy);
        policies.add(rp);
        return rp;
    }

    
    public synchronized boolean removeById(int id) {
        return policies.removeIf(p -> p.id == id);
    }

    
    public synchronized List<RegisteredPolicy> list() {
        return Collections.unmodifiableList(new ArrayList<>(policies));
    }

   
    public static final class RegisteredPolicy {
        public final int id;
        public final String name;
        public final DiscountPolicy policy;

        private RegisteredPolicy(int id, String name, DiscountPolicy policy) {
            this.id = id;
            this.name = name;
            this.policy = policy;
        }

        
        public String displayLabel() {
            boolean active = isActiveNow();
            Double percent = extractPercent();
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            if (percent != null) {
                sb.append(" (").append(String.format("%.2f%%", percent)).append(")");
            }
            sb.append(active ? " [Active]" : " [Inactive]");
            return sb.toString();
        }

        
        public Double extractPercent() {
            if (policy instanceof PercentageDiscountPolicy p) {
                return p.getPercent();
            }
            if (policy instanceof DatedHappyHourDiscountPolicy p) {
                return p.getPercent();
            }
            return null;
        }

        
        public boolean isActiveNow() {
            if (policy instanceof DatedHappyHourDiscountPolicy p) {
                return p.isActiveNow();
            }
            // Percentage or other static policies considered always active
            return true;
        }
    }
}