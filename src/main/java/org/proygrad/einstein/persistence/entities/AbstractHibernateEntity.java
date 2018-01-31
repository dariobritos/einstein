package org.proygrad.einstein.persistence.entities;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Transient;
import java.io.Serializable;

public abstract class AbstractHibernateEntity<ID extends Serializable>
                implements HibernateEntity<ID> {

    @Override
    public abstract ID getId();

    @Override
    public abstract void setId(ID id);

    /**
     * Uses ID to evaluate equality.<br/>
     * Uses commons-lang EqualsBuilder, generic method, should be overridden for custom behavior.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // Basic early-cool-checking...
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        // If not a IdentifiablePersistentObject... I don't care, you're not equal to me...
        if (!(obj instanceof AbstractHibernateEntity)) {
            return false;
        }

        // This conditions fails with Hibernate3 Lazy & L2 Cache:
        //
        // >> if (obj.getClass() != this.getClass()) {
        // This is a workaround is to avoid false evaluation for a PersistentObject and its subclass built by CGLib.
        //
        // Note: If the classes are different, the only exception to the rule is to be an instanceof HibernateProxy and
        // a subclass of "this".
        Class<?> objClass = this.getNonProxyClass(obj);
        Class<?> thisClass = this.getNonProxyClass(this);
        if (!objClass.equals(thisClass)) {
            return false;
        }
        AbstractHibernateEntity<ID> other = (AbstractHibernateEntity<ID>) obj;
        // Check if both ids are null, if they are, we can't assume that are equal.
        ID thisId = this.getId();
        ID otherId = other.getId();
        if (thisId == null || otherId == null) {
            return false;
        }
        // Evaluate the ids.
        // EqualsBuilder has a bug that first compares object with == and then checks nulls.
        return new EqualsBuilder().append(thisId, otherId).isEquals();
    }

    /**
     * Uses Class.hashCode & OID to generate the hashCode.<br/>
     * Uses commons-lang HashCodeBuilder, generic method, should be overridden for custom behavior.
     */
    @Override
    public int hashCode() {
        String className = this.getNonProxyClass(this).getName();
        // Uses the "class" hashCode as a super.hashCode...
        return new HashCodeBuilder().appendSuper(className.hashCode()).append(this.getId()).toHashCode();
    }

    /**
     * Uses OID & ClassName<br/>
     * Uses commons-lang ToStringBuilder, generic method, should be overridden for custom behavior.<br/>
     * Style {@link ToStringBuilder.SHORT_PREFIX_STYLE} format.
     */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("id", this.getId());
        this.paramString(builder);
        return builder.toString();
    }

    /**
     * Used by toString method, should be overridden for custom behavior.
     */
    protected void paramString(ToStringBuilder builder) {
    }

    /**
     * Helper persistent object method.<br/>
     * Determines the real concrete class behind a persistent object class, it could be a {@link HibernateProxy} and we
     * need the super class.
     *
     * @param obj
     * @return
     */
    @Transient
    protected Class<?> getNonProxyClass(Object obj) {
        if (obj instanceof HibernateProxy) {
            return obj.getClass().getSuperclass();
        }
        return obj.getClass();
    }


}
