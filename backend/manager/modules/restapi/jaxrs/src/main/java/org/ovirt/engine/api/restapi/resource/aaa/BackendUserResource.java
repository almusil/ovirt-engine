package org.ovirt.engine.api.restapi.resource.aaa;

import javax.ws.rs.core.Response;

import org.ovirt.engine.api.model.BaseResource;
import org.ovirt.engine.api.model.User;
import org.ovirt.engine.api.resource.AssignedPermissionsResource;
import org.ovirt.engine.api.resource.AssignedRolesResource;
import org.ovirt.engine.api.resource.AssignedTagsResource;
import org.ovirt.engine.api.resource.aaa.DomainUserGroupsResource;
import org.ovirt.engine.api.resource.aaa.SshPublicKeysResource;
import org.ovirt.engine.api.resource.aaa.UserResource;
import org.ovirt.engine.api.restapi.resource.AbstractBackendSubResource;
import org.ovirt.engine.api.restapi.resource.BackendAssignedPermissionsResource;
import org.ovirt.engine.api.restapi.resource.BackendAssignedRolesResource;
import org.ovirt.engine.api.restapi.resource.BackendUserTagsResource;
import org.ovirt.engine.core.common.action.ActionType;
import org.ovirt.engine.core.common.action.IdParameters;
import org.ovirt.engine.core.common.businessentities.aaa.DbUser;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.QueryType;

/**
 * This resource corresponds to an user that has been added to the engine and
 * stored in the database.
 */
public class BackendUserResource
        extends AbstractBackendSubResource<User, DbUser>
        implements UserResource {

    private BackendUsersResource parent;

    public BackendUserResource(String id, BackendUsersResource parent) {
        super(id, User.class, DbUser.class);
        this.parent = parent;
    }

    public void setParent(BackendUsersResource parent) {
        this.parent = parent;
    }

    public BackendUsersResource getParent() {
        return parent;
    }

    @Override
    public User get() {
        return performGet(QueryType.GetDbUserByUserId, new IdQueryParameters(guid), BaseResource.class);
    }

    @Override
    public AssignedRolesResource getRolesResource() {
        return inject(new BackendAssignedRolesResource(guid));
    }

    @Override
    public AssignedTagsResource getTagsResource() {
        return inject(new BackendUserTagsResource(id));
    }

    @Override
    public AssignedPermissionsResource getPermissionsResource() {
        return inject(new BackendAssignedPermissionsResource(guid,
                                                             QueryType.GetPermissionsOnBehalfByAdElementId,
                                                             new IdQueryParameters(guid),
                                                             User.class));
    }

    @Override
    public SshPublicKeysResource getSshPublicKeysResource() {
        return inject(new BackendSSHPublicKeysResource(guid));
    }

    @Override
    public Response remove() {
        get();
        return performAction(ActionType.RemoveUser, new IdParameters(guid));
    }

    @Override
    public DomainUserGroupsResource getGroupsResource() {
        return null;
    }
}
