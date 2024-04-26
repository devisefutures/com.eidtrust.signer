package eu.assina.rssp.repository;

import eu.assina.rssp.common.model.AssinaCredential;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data Repository with paging and sorting support for Credential entities with String identifiers.
 *
 * Notice that there is no implementation of this interface: Spring Data implements it automatically
 * and translates both the inherited CRUD methods and the custom query methods here to underlying
 * JPA queries.
 *
 * The queries are fired against a database that is automatically connected by the
 * mere declaration of a dependency (on H2 in this case) in the root pom.xml.
 */
public interface CredentialRepository extends PagingAndSortingRepository<AssinaCredential, String>
{
    /**
     * Find a certificate by Owner.
     * <p>
     * This takes advantage of Spring Data's automatic mapping of method names to queries.
     * Note that it returns a page of results despite there generally being only a single cert.
     *
     * @param owner owner id
     * @param pageable pagination and query results
     * @return a page of results with a pagination properties
     * @see <a https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods">https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods</a>
     */
    Page<AssinaCredential> findByOwner(String owner, Pageable pageable);

    long countByOwner(String owner);
}
